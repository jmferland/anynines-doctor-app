INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Lorenz', 'Gerteis', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Mark', 'Fisher', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Rod', 'Johnson', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'David', 'Syer', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Gunnar', 'Hillert', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Dave', 'McCrory', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Josh', 'Long', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Patrick', 'Chanezon', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Andy', 'Piper', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Eric', 'Bottard', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Chris', 'Richardson', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Raja', 'Rao', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Rajdeep', 'Dua', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Monica', 'Wilkinson', NOW());
INSERT INTO customer(id, firstname, lastname, signupdate) values( nextval( 'hibernate_sequence') , 'Mark', 'Pollack', NOW());

-- INSERT INTO merchant(id, name, creationdate, securitysender, userlogin, userpassword, channelid) values( nextval( 'hibernate_sequence') , 'CNP IG' , NOW() , '696a8f0fabffea91517d0eb0a0bf9c33' , '1143238d620a572a726fe92eede0d1ab' , 'demo' , '52275ebaf361f20a76b038ba4c806991');
INSERT INTO merchant(id, name, creationdate, securitysender, userlogin, userpassword, channelid) values( nextval( 'hibernate_sequence') , 'Dr. med. Elena Voss' , NOW() , 'ff8080814107fbba0141300c54bf3395' , 'ff8080814107fbba0141300c54c03399' , 'QBf6wNZX' , 'ff8080814107fbba0141300c8166339c');

INSERT INTO bill(id, merchant_id, customer_id, creationDate, token, descriptor, amount, currency) values( nextval( 'hibernate_sequence') , (SELECT m.id FROM merchant m WHERE m.name = 'Dr. med. Elena Voss') , (SELECT c.id FROM customer c WHERE c.lastName = 'Gerteis') , to_date('11 Sep 2013', 'DD Mon YYYY') , '00000000000000000000000000000000' , 'Dr. med. Voss; Invoice Nr. 45230304' , 10.00 , 'EUR');
INSERT INTO bill(id, merchant_id, customer_id, creationDate, token, descriptor, amount, currency) values( nextval( 'hibernate_sequence') , (SELECT m.id FROM merchant m WHERE m.name = 'Dr. med. Elena Voss') , (SELECT c.id FROM customer c WHERE c.lastName = 'Gerteis') , to_date('13 Sep 2013', 'DD Mon YYYY') , '00000000000000000000000000000001' , 'Dr. med. Voss; Invoice Nr. 45230305' , 63.00 , 'EUR');
INSERT INTO bill(id, merchant_id, customer_id, creationDate, token, descriptor, amount, currency) values( nextval( 'hibernate_sequence') , (SELECT m.id FROM merchant m WHERE m.name = 'Dr. med. Elena Voss') , (SELECT c.id FROM customer c WHERE c.lastName = 'Fisher') , to_date('19 Sep 2013', 'DD Mon YYYY') , '00000000000000000000000000000002' , 'Dr. med. Voss; Invoice Nr. 45230306' , 77.43 , 'EUR');

-- TODO: replace RG with one for this new channel
-- TODO: can RG be used between channels?
INSERT INTO registration(id, customer_id, creationDate, code, brand, bin, last4Digits, month, year) values ( nextval( 'hibernate_sequence') , (SELECT c.id FROM customer c WHERE c.lastName = 'Fisher') , NOW() , 'ff8080814125c69301412d548ea86762' , 'VISA' , '420000' , '0000', '11' , '2017');