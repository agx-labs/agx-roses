ALTER TABLE `sys_resource`
MODIFY COLUMN `validate_groups` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '需要进行参数校验的分组' AFTER `permission_code`,
MODIFY COLUMN `param_field_descriptions` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口参数的字段描述' AFTER `validate_groups`,
MODIFY COLUMN `response_field_descriptions` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接口返回结果的字段描述' AFTER `param_field_descriptions`;