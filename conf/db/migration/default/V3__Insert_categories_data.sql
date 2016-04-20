INSERT INTO categories (id, name, parent_id) VALUES
  (nextval('categories_id_seq'), 'Pakaian Wanita', NULL), -- id = 1
  (nextval('categories_id_seq'), 'Pakaian Pria', NULL), -- id = 2
  (nextval('categories_id_seq'), 'Tas & Aksesoris', NULL), -- id = 3
  (nextval('categories_id_seq'), 'Gaun & Terusan', 1), -- id = 4
  (nextval('categories_id_seq'), 'Atasan', 1), -- id = 5
  (nextval('categories_id_seq'), 'Batik', 1), -- id = 6
  (nextval('categories_id_seq'), 'Pakaian Tidur & Pakaian Dalam', 1), -- id = 7
  (nextval('categories_id_seq'), 'Pakaian Hamil & Menyusui', 1), -- id = 8
  (nextval('categories_id_seq'), 'Jaket & blazer', 2), -- id = 9
  (nextval('categories_id_seq'), 'Celana Pria', 2), -- id = 10
  (nextval('categories_id_seq'), 'Sweater & Kardigan', 2), -- id = 11
  (nextval('categories_id_seq'), 'Kaos Pria', 2), -- id = 12
  (nextval('categories_id_seq'), 'Kemeja Pria', 2), -- id = 13
  (nextval('categories_id_seq'), 'Kacamata & Lensa Kontak', 3), -- id = 14
  (nextval('categories_id_seq'), 'Kaos Kaki & Stoking', 3), -- id = 15
  (nextval('categories_id_seq'), 'Syal & Sarung Tangan', 3), -- id = 16
  (nextval('categories_id_seq'), 'Tas Penyimpanan', 3), -- id = 17
  (nextval('categories_id_seq'), 'Gaun Mini', 4),
  (nextval('categories_id_seq'), 'Gaun Midi', 4),
  (nextval('categories_id_seq'), 'Gaun Maxi', 4),
  (nextval('categories_id_seq'), 'Terusan Setelan', 4),
  (nextval('categories_id_seq'), 'Baju Setelan', 4),
  (nextval('categories_id_seq'), 'Baby Dolls', 4),
  (nextval('categories_id_seq'), 'Gaun Pesta', 4),
  (nextval('categories_id_seq'), 'Gaun Tanpa Lengan', 4),
  (nextval('categories_id_seq'), 'Gaun Lengan panjang', 4),
  (nextval('categories_id_seq'), 'Gaun Lengan Pendek', 4),
  (nextval('categories_id_seq'), 'Blus Wanita', 5),
  (nextval('categories_id_seq'), 'Kemeja Formal Wanita', 5),
  (nextval('categories_id_seq'), 'Tunik Wanita', 5),
  (nextval('categories_id_seq'), 'Kemeja Casual Wanita', 5),
  (nextval('categories_id_seq'), 'Blouse Batik', 6),
  (nextval('categories_id_seq'), 'Kemeja Batik', 6),
  (nextval('categories_id_seq'), 'Kebaya', 6),
  (nextval('categories_id_seq'), 'Batik Songket', 6),
  (nextval('categories_id_seq'), 'Kaos Batik', 6),
  (nextval('categories_id_seq'), 'Pakaian Tidur Wanita', 7),
  (nextval('categories_id_seq'), 'Piyama', 7),
  (nextval('categories_id_seq'), 'Bathrobe Wanita', 7),
  (nextval('categories_id_seq'), 'Bra', 7),
  (nextval('categories_id_seq'), 'Baju Terusan', 8),
  (nextval('categories_id_seq'), 'Baju Atasan', 8),
  (nextval('categories_id_seq'), 'Bawahan', 8),
  (nextval('categories_id_seq'), 'Pakaian Dalam Ibu Hamil', 8),
  (nextval('categories_id_seq'), 'Bra Menyusui', 8),
  (nextval('categories_id_seq'), 'Atasan Menyusui', 8),
  (nextval('categories_id_seq'), 'Jaket Outdoor Pria', 9),
  (nextval('categories_id_seq'), 'Jaket Parka Pria', 9),
  (nextval('categories_id_seq'), 'Mantel Pria', 9),
  (nextval('categories_id_seq'), 'Jaket Parasut Pria', 9),
  (nextval('categories_id_seq'), 'Jas Pria', 9),
  (nextval('categories_id_seq'), 'Celana Pendek Pria', 10),
  (nextval('categories_id_seq'), 'Celana Chino Pria', 10),
  (nextval('categories_id_seq'), 'Celana Pria lainnya', 10),
  (nextval('categories_id_seq'), 'Celana cargo pria', 10),
  (nextval('categories_id_seq'), 'Celana Bahan', 10),
  (nextval('categories_id_seq'), 'Hoodie Pria', 11),
  (nextval('categories_id_seq'), 'Cardigan Pria', 11),
  (nextval('categories_id_seq'), 'Sweater Pria', 11),
  (nextval('categories_id_seq'), 'Kaos Lengan panjang', 12),
  (nextval('categories_id_seq'), 'Kaos berkerah', 12),
  (nextval('categories_id_seq'), 'Kaos lengan pendek pria', 12),
  (nextval('categories_id_seq'), 'Kaos Polo Pria', 12),
  (nextval('categories_id_seq'), 'Kaos t-shirt', 12),
  (nextval('categories_id_seq'), 'Kaos Singlet', 12),
  (nextval('categories_id_seq'), 'Kemeja Lengan Pendek', 13),
  (nextval('categories_id_seq'), 'Kemeja Lengan Panjang', 13),
  (nextval('categories_id_seq'), 'Kemeja Flanel Pria', 13),
  (nextval('categories_id_seq'), 'Kemeja Formal Pria', 13),
  (nextval('categories_id_seq'), 'Kemeja Casual Pria', 13),
  (nextval('categories_id_seq'), 'Tempat Kacamata & Lensa', 14),
  (nextval('categories_id_seq'), 'Lensa Kontak', 14),
  (nextval('categories_id_seq'), 'Perlengkapan Lensa Kontak', 14),
  (nextval('categories_id_seq'), 'Kacamata Baca', 14),
  (nextval('categories_id_seq'), 'Kaos Kaki', 15),
  (nextval('categories_id_seq'), 'Kaos Kaki Sekolah', 15),
  (nextval('categories_id_seq'), 'Stoking & Kaos Kaki Wanita', 15),
  (nextval('categories_id_seq'), 'Syal / Shawl', 16),
  (nextval('categories_id_seq'), 'Sarung Tangan', 16),
  (nextval('categories_id_seq'), 'Tas Bahu Travel', 17),
  (nextval('categories_id_seq'), 'Tas Ransel Travel', 17),
  (nextval('categories_id_seq'), 'Koper Ukuran Kabin', 17),
  (nextval('categories_id_seq'), 'Koper', 17),
  (nextval('categories_id_seq'), 'Koper Set', 17)
















