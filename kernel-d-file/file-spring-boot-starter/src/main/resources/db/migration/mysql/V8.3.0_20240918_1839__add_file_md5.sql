ALTER TABLE `sys_file_info`
ADD COLUMN `file_md5` varchar(100) NULL COMMENT '文件的md5值' AFTER `secret_flag`;