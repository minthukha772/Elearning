-- Master Account
INSERT INTO public.user_account(account_id, account_status, is_mail_verified, mail, password, registered_date, role) 
VALUES (0, 'Registered',TRUE,'SuperAdmin@gmail.com','$2a$10$T6ues0iAD9jXnIqxGWPzzOtppcA5pDx6YqmE/c8J/hYyIP2Z8ZhNW','2021-12-01','ROLE_SUPER_ADMIN');

-- Bank Info
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (1,'Kpay');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (2,'Cbpay');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (3,'Wave');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (4,'KBZ');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (5,'CB');
INSERT INTO public.bank_mst(bank_id, bank_name)VALUES (6,'AYA');


