CREATE TABLE `sys_user_favorite`  (
  `favorite_id` bigint NOT NULL COMMENT '主键id',
  `fav_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收藏业务的类型，存业务编码',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `business_id` bigint NOT NULL COMMENT '具体业务id',
  `fav_time` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`favorite_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户收藏信息' ROW_FORMAT = Dynamic;