INSERT INTO books (id, title, author, amount)
VALUES (1, 'The Lord of the Rings', 'J.R.R. Tolkien', 1),
       (2, 'Harry Potter and the Philosopher''s Stone', 'J.K. Rowling', 1);

INSERT INTO members(id, name, surname, membership_date)
VALUES (1, 'John', 'Doe', '2024-04-24T22:22:09.266615Z'),
       (2, 'Jane', 'Doe', '2024-04-24T22:22:09.266615Z');

INSERT INTO borrowed_books (id, member_id, book_id, borrowed_date, returned_date)
VALUES (1, 1, 1, '2024-04-24T22:22:09.266615Z', '2024-04-24T22:28:19.266615Z');
