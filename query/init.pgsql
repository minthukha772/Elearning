-- Bank Info
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (1,'Kpay');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (2,'Cbpay');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (3,'Wave');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (4,'KBZ');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (5,'CB');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (6,'AYA');

-- User Account
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10001,'REGISTERED',TRUE,'Rachel@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-10','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10002,'REGISTERED',FALSE,'Michel@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-10','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10003,'SUSPENDED',TRUE,'Bella@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-10','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10004,'REGISTERED',TRUE,'James@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-11','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10005,'REGISTERED',TRUE,'Christine@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-11','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10006,'REGISTERED',TRUE,'Zet@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-12','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10007,'REGISTERED',TRUE,'Lin@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-13','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10008,'REGISTERED',TRUE,'Htet@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-13','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10009,'REGISTERED',TRUE,'Myat@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-13','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10010,'REGISTERED',TRUE,'Min@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-13','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10011,'REGISTERED',FALSE,'Moh@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-13','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10012,'REGISTERED',TRUE,'Mann@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-14','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10013,'REGISTERED',TRUE,'Thu@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-15','ROLE_STUDENT','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10014,'VERIFIED',TRUE,'Aye@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-15','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10015,'REQUESTED',TRUE,'Pwint@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-16','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (10016,'VERIFIED',TRUE,'Hnin@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-18','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (20001,'REQUESTED',TRUE,'Zyan@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-18','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (20002,'VERIFIED',TRUE,'Thant@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-20','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (20003,'REQUESTED',TRUE,'Htoo@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-20','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (20004,'VERIFIED',TRUE,'Moe@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-20','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (20005,'REQUESTED',TRUE,'Myint@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-20','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (20006,'VERIFIED',TRUE,'Dali@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-20','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (20007,'REQUESTED',TRUE,'Chaint@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-23','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (20008,'VERIFIED',TRUE,'Diana@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-23','ROLE_TEACHER','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (30013,'REGISTERED',TRUE,'Admin13@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-23','ROLE_ADMIN','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (30014,'REGISTERED',TRUE,'Admin14@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-27','ROLE_ADMIN','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (30015,'REGISTERED',TRUE,'Admin15@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-28','ROLE_ADMIN','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (30016,'REGISTERED',TRUE,'Admin16@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-11-28','ROLE_ADMIN','photo_sample');
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role, photo) VALUES (40001,'REGISTERED',TRUE,'SuperAdmin@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-12-01','ROLE_SUPER_ADMIN','photo_sample');

-- User Info
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10001,'123st','2021-11-10','Yangon','Yangon','Fourth Year','female','12/TTT(Naing)111111','9420099999',null,'11211','About myself','Rachel');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10002,'124st','20001-01-01','Yangon','Yangon','Third Year','male','12/TTT(Naing)111112','9420100000',null,'11212','About myself','Michel');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10003,'125st','2002-01-01','Yangon','Yangon','Second Year','female','12/TTT(Naing)111113','9420100001',null,'11213','About myself','Bella');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10004,'126st','1990-01-01','Yangon','Yangon','First Year','male','12/TTT(Naing)111114','9420100002',null,'11214','About myself','James');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10005,'127st','2000-01-01','Yangon','Yangon','Grade 11','female','12/TTT(Naing)111115','9420100003',null,'11215','About myself','Christine');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10006,'128st','2001-10-11','Yangon','Yangon','Grade 10','male','12/TTT(Naing)111116','9420100004',null,'11216','About myself','Zet');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10007,'129st','2003-01-01','Yangon','Yangon','Grade 9','female','12/TTT(Naing)111117','9420100005',null,'11217','About myself','Lin');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10008,'130st','2004-02-05','Yangon','Yangon','Grade 8','male','12/TTT(Naing)111118','9420100006',null,'11218','About myself','Htet');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10009,'131st','2002-10-01','Yangon','Yangon','Grade 7','female','12/TTT(Naing)111119','9420100007',null,'11219','About myself','Myat');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10010,'132st','2001-11-03','Yangon','Yangon','Grade 6','male','12/TTT(Naing)111120','9420100008',null,'11220','About myself','Min');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10011,'133st','1994-03-01','Yangon','Yangon','Grade 5','female','12/TTT(Naing)111121','9420100009',null,'11221','About myself','Moh');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10012,'134st','2001-09-01','Yangon','Yangon','Grade 4','male','12/TTT(Naing)111122','9420100010',null,'11222','About myself','Mann');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10013,'135st','2002-01-01','Yangon','Yangon','Grade 3','female','12/TTT(Naing)111123','9420100011',null,'11223','About myself','Thu');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10014,'136st','2001-01-01','Yangon','Yangon','Grade 2','male','12/TTT(Naing)111124','9420100012',null,'11224','About myself','Aye');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10015,'137st','2002-01-01','Yangon','Yangon','Grade 1','female','12/TTT(Naing)111125','9420100013',null,'11225','About myself','Pwint');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (10016,'138st','2001-01-01','Yangon','Yangon','KG','male','12/TTT(Naing)111126','9420100014',null,'11226','About myself','Hnin');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (20001,'139st','1999-06-01','Yangon','Yangon','Graduated','female','12/TTT(Naing)111127','9420100015',null,'11227','About myself','Zyan');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (20002,'140st','1987-05-22','Yangon','Yangon','Graduated','male','12/TTT(Naing)111128','9420100016',null,'11228','About myself','Thant');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (20003,'141st','1992-05-02','Yangon','Yangon','Graduated','female','12/TTT(Naing)111129','9420100017',null,'11229','About myself','Htoo');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (20004,'142st','1988-04-03','Yangon','Yangon','Graduated','female','12/TTT(Naing)111130','9420100018',null,'11230','About myself','Moe');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (20005,'143st','1985-08-10','Yangon','Yangon','Graduated','male','12/TTT(Naing)111131','9420100019',null,'11231','About myself','Myint');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (20006,'144st','1997-12-01','Yangon','Yangon','Graduated','female','12/TTT(Naing)111132','9420100020',null,'11232','About myself','Dali');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (20007,'145st','1994-02-03','Yangon','Yangon','Graduated','male','12/TTT(Naing)111133','9420100021',null,'11233','About myself','Chaint');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (20008,'146st','1998-04-21','Yangon','Yangon','Graduated','female','12/TTT(Naing)111134','9420100022',null,'11234','About myself','Diana');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (30013,'147st','1989-04-01','Yangon','Yangon','Graduated','male','12/TTT(Naing)111135','9420100023',null,'11235','About myself','Christ');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (30014,'148st','1995-10-01','Yangon','Yangon','Graduated','female','12/TTT(Naing)111136','9420100024',null,'11236','About myself','Athen');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (30015,'149st','1994-10-01','Yangon','Yangon','Graduated','male','12/TTT(Naing)111137','9420100025',null,'11237','About myself','Bhone');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (30016,'150st','1992-11-25','Yangon','Yangon','Graduated','female','12/TTT(Naing)111138','9420100026',null,'11238','About myself','Lisa');
INSERT INTO public.user_info(account_id, address, birth_date, city, division, education, gender, nrc, phone_no, photo, postal_code, self_description, user_name)VALUES (40001,'151st','1999-06-01','Yangon','Yangon','Graduated','male','12/TTT(Naing)111139','9420100027',null,'11239','About myself','Juila');





















