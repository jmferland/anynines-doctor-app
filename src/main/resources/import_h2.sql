INSERT INTO customer(firstname, lastname, signupdate) values ('Lorenz','Gerteis', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Mark','Fisher', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Rod','Johnson', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('David','Syer', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Gunnar','Hillert', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Dave','McCrory', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Josh','Long', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Patrick','Chanezon', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Andy','Piper', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Eric','Bottard', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Chris','Richardson', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Raja','Rao', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Rajdeep','Dua', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Monica','Wilkinson', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Mark','Pollack', NOW() );

-- INSERT INTO merchant(name, creationdate, securitysender, userlogin, userpassword, channelid) values( 'CNP IG' , NOW() , '696a8f0fabffea91517d0eb0a0bf9c33' , '1143238d620a572a726fe92eede0d1ab' , 'demo' , '52275ebaf361f20a76b038ba4c806991');
INSERT INTO merchant(name, creationdate, securitysender, userlogin, userpassword, channelid) values( 'Dr. med. Elena Voss' , NOW() , 'ff8080814107fbba0141300c54bf3395' , 'ff8080814107fbba0141300c54c03399' , 'QBf6wNZX' , 'ff8080814107fbba0141300c8166339c');

INSERT INTO bill(merchant_id, customer_id, creationDate, token, descriptor, amount, currency) values( (SELECT m.id FROM merchant m WHERE m.name = 'Dr. med. Elena Voss') , (SELECT c.id FROM customer c WHERE c.lastName = 'Gerteis') , to_date('19 Sep 2013', 'DD Mon YYYY') , '00000000000000000000000000000000' , 'Dr. med. Voss; Invoice Nr. 45230304' , 62.63 , 'EUR');
INSERT INTO bill(merchant_id, customer_id, creationDate, token, descriptor, amount, currency) values( (SELECT m.id FROM merchant m WHERE m.name = 'Dr. med. Elena Voss') , (SELECT c.id FROM customer c WHERE c.lastName = 'Gerteis') , to_date('20 Sep 2013', 'DD Mon YYYY') , '00000000000000000000000000000001' , 'Dr. med. Voss; Invoice Nr. 45230304' , 111.49 , 'EUR');
INSERT INTO bill(merchant_id, customer_id, creationDate, token, descriptor, amount, currency) values( (SELECT m.id FROM merchant m WHERE m.name = 'Dr. med. Elena Voss') , (SELECT c.id FROM customer c WHERE c.lastName = 'Fisher') , to_date('19 Sep 2013', 'DD Mon YYYY') , '00000000000000000000000000000002' , 'Dr. med. Voss; Invoice Nr. 45230305' , 77.43 , 'EUR');

INSERT INTO registration(customer_id, creationDate, code, brand, bin, last4Digits, expiryMonth, expiryYear) values ( (SELECT c.id FROM customer c WHERE c.lastName = 'Fisher') , NOW() , 'ff8080814125c69301412d548ea86762' , 'VISA' , '420000' , '0000', '11', '2017');