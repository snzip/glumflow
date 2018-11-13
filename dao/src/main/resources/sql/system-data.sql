/** SYSTEM **/

/** System admin **/
INSERT INTO tb_user ( id, tenant_id, customer_id, email, search_text, authority )
VALUES ( '1e746125a797660a91992ebcb67fe33', '1b21dd2138140008080808080808080', '1b21dd2138140008080808080808080', 'sysadmin@thingsboard.org',
         'sysadmin@thingsboard.org', 'SYS_ADMIN' );

INSERT INTO user_credentials ( id, user_id, enabled, password )
VALUES ( '1e7461261441950a91992ebcb67fe33', '1e746125a797660a91992ebcb67fe33', true,
         '$2a$10$5JTB8/hxWc9WAy62nCGSxeefl3KWmipA9nFpVdDa0/xfIseeBB4Bu' );

/** System settings **/
INSERT INTO admin_settings ( id, key, json_value )
VALUES ( '1e746126a2266e4a91992ebcb67fe33', 'general', '{
	"baseUrl": "http://localhost:8080"
}' );

INSERT INTO admin_settings ( id, key, json_value )
VALUES ( '1e746126eaaefa6a91992ebcb67fe33', 'mail', '{
	"mailFrom": "Thingsboard <sysadmin@localhost.localdomain>",
	"smtpProtocol": "smtp",
	"smtpHost": "localhost",
	"smtpPort": "25",
	"timeout": "10000",
	"enableTls": "false",
	"username": "",
	"password": ""
}' );
