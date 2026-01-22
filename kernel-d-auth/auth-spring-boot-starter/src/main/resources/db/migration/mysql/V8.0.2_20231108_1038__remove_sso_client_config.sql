delete from sys_config where config_code = 'SYS_AUTH_SSO_JWT_SECRET';
UPDATE `sys_dict` SET  `dict_name` = '单点客户端配置' WHERE `dict_id` = 1526221204984197121;