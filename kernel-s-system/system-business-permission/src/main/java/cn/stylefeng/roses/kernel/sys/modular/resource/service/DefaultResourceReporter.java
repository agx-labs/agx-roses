package cn.stylefeng.roses.kernel.sys.modular.resource.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.scanner.api.ResourceReportApi;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.ReportResourceParam;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.ResourceDefinition;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.SysResourcePersistencePojo;
import cn.stylefeng.roses.kernel.sys.modular.resource.entity.SysResource;
import cn.stylefeng.roses.kernel.sys.modular.resource.factory.ResourceFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 资源扫描的搜集
 *
 * @author fengshuonan
 * @since 2023/6/18 10:34
 */
@Service
@Slf4j
public class DefaultResourceReporter implements ResourceReportApi {

    @Resource
    private SysResourceService sysResourceService;

    @Resource(name = "resourceCache")
    private CacheOperatorApi<ResourceDefinition> resourceCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportResources(@RequestBody ReportResourceParam reportResourceReq) {
        this.reportResourcesAndGetResult(reportResourceReq);
    }

    @Override
    public List<SysResourcePersistencePojo> reportResourcesAndGetResult(ReportResourceParam reportResourceReq) {
        String projectCode = reportResourceReq.getProjectCode();
        Map<String, Map<String, ResourceDefinition>> resourceDefinitions = reportResourceReq.getResourceDefinitions();

        if (ObjectUtil.isEmpty(projectCode) || resourceDefinitions == null) {
            return new ArrayList<>();
        }

        // 获取当前应用的所有资源
        ArrayList<SysResource> allResources = new ArrayList<>();
        ArrayList<ResourceDefinition> resourceDefinitionArrayList = new ArrayList<>();
        for (Map.Entry<String, Map<String, ResourceDefinition>> appModularResources : resourceDefinitions.entrySet()) {
            Map<String, ResourceDefinition> value = appModularResources.getValue();
            for (Map.Entry<String, ResourceDefinition> modularResources : value.entrySet()) {
                resourceDefinitionArrayList.add(modularResources.getValue());
                SysResource resource = ResourceFactory.createResource(modularResources.getValue());
                allResources.add(resource);
            }
        }

        CompletableFuture.runAsync(() -> {
            // 根据project删除该项目下的所有资源
            this.sysResourceService.deleteResourceByProjectCode(projectCode);

            // 将资源存入库中
            sysResourceService.batchSaveResourceList(allResources);
        }).exceptionally(ex -> {
            log.error("资源扫描保存到库中出现错误，请检查问题，以免影响后续运行！", ex);
            return null;
        });

        // 将资源存入缓存一份
        Map<String, ResourceDefinition> resourceDefinitionMap = ResourceFactory.orderedResourceDefinition(resourceDefinitionArrayList);
        for (Map.Entry<String, ResourceDefinition> entry : resourceDefinitionMap.entrySet()) {
            resourceCache.put(entry.getKey(), entry.getValue());
        }

        // 组装返回结果
        ArrayList<SysResourcePersistencePojo> finalResult = new ArrayList<>();
        for (SysResource item : allResources) {
            SysResourcePersistencePojo sysResourcePersistencePojo = new SysResourcePersistencePojo();
            BeanUtil.copyProperties(item, sysResourcePersistencePojo);
            finalResult.add(sysResourcePersistencePojo);
        }

        return finalResult;
    }

}
