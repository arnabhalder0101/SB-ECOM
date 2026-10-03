The fundamental difference between @JoinColumn and @JoinTable.
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Here is a side-by-side comparison of how each affects your database schema:

1. @JoinColumn — Modifies the Existing Table
Used primarily in @ManyToOne or @OneToOne relationships.

What it means: It does not create a new table.

What it does: It adds a physical foreign key column directly into the current entity's existing SQL table.

Your example: In a Product entity with @ManyToOne and @JoinColumn(name = "category_id"), the database products table physically gains a new column:

Plaintext
products table
+------------+--------------+---------------+
| product_id | product_name |  category_id  |  <-- Placed directly in products
+------------+--------------+---------------+
|     1      | Laptop       |      10       |
+------------+--------------+---------------+
2. @JoinTable — Creates a Brand New Bridge Table
Used primarily in @ManyToMany relationships (and occasionally unidirectional @OneToMany).

What it means: Neither users nor roles gets a foreign key column referencing the other.

What it does: JPA generates a dedicated third table (user_role) containing two foreign keys that map pairs together.

Your code:

Plaintext
users table             user_role (Bridge Table)            roles table
+---------+----------+  +---------+---------+               +---------+-----------+
| user_id | userName |  | user_id | role_id |               | role_id | role_name |
+---------+----------+  +---------+---------+               +---------+-----------+
|    1    | John     |  |    1    |   101   | ------------> |   101   | ROLE_USER |
|    2    | Sara     |  |    1    |   102   | ------------> |   102   | ROLE_ADMIN|
+---------+----------+  |    2    |   101   |               +---------+-----------+
                        +---------+---------+
Notice that users still only has user_id, user_name, email, and password. It contains no role_id column.
