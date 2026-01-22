ALTER TABLE `sys_user` ADD COLUMN `employee_number` varchar(100) NULL COMMENT '工号' AFTER `user_sort`;

CREATE TABLE `sys_user_certificate`  (
  `user_certificate_id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `certificate_type` bigint NOT NULL COMMENT '证书类型，取字典id',
  `certificate_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证书编号',
  `issuing_authority` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发证机构名称',
  `date_issued` date NULL DEFAULT NULL COMMENT '证书发证日期',
  `date_expires` date NULL DEFAULT NULL COMMENT '证书到期日期，如果为空，则为长期',
  `attachment_id` bigint NULL DEFAULT NULL COMMENT '文件id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否删除：Y-删除，N-未删除',
  PRIMARY KEY (`user_certificate_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户证书' ROW_FORMAT = Dynamic;

INSERT INTO `sys_dict_type`(`dict_type_id`, `dict_type_class`, `dict_type_bus_code`, `dict_type_code`, `dict_type_name`, `dict_type_name_pinyin`, `dict_type_desc`, `status_flag`, `dict_type_sort`, `version_flag`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1722790763315597314, 1, NULL, 'certificate_type', '证书类型', 'zslx', NULL, 1, 105.00, 0, 'N', '2023-11-10 09:38:21', 1339550467939639299, NULL, NULL);
INSERT INTO `sys_dict`(`dict_id`, `dict_type_id`, `dict_code`, `dict_name`, `dict_name_pinyin`, `dict_encode`, `dict_short_name`, `dict_short_code`, `dict_parent_id`, `dict_pids`, `status_flag`, `dict_sort`, `version_flag`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1722790809880760322, 1722790763315597314, 'A', '证书类型A', 'zslxA', NULL, NULL, NULL, -1, '[-1],', 1, 100.00, 0, 'N', '2023-11-10 09:38:32', 1339550467939639299, NULL, NULL);
INSERT INTO `sys_dict`(`dict_id`, `dict_type_id`, `dict_code`, `dict_name`, `dict_name_pinyin`, `dict_encode`, `dict_short_name`, `dict_short_code`, `dict_parent_id`, `dict_pids`, `status_flag`, `dict_sort`, `version_flag`, `del_flag`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1722790840398516225, 1722790763315597314, 'B', '证书类型B', 'zslxB', NULL, NULL, NULL, -1, '[-1],', 1, 100.00, 0, 'N', '2023-11-10 09:38:39', 1339550467939639299, NULL, NULL);
