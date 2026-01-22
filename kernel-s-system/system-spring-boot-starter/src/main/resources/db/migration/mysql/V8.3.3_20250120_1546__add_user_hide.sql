ALTER TABLE `sys_user`
ADD COLUMN `hidden_flag` char(1) NULL DEFAULT 'N' COMMENT '是否隐藏：Y-隐藏，N-不隐藏，用在系统内置账号' AFTER `master_user_id`;