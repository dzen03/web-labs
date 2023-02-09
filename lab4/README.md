# EJB + Angular + *bootstrap (voluntarily)*

Bootstrap just for easier optimisation for different screen sizes.

You need to fix **all TODOs** (`back/.../persistance.xml` - database url and a couple of files in `front` - server url[^1]).

Also, I'm using `nx` - buld system insead of `npm`.


### Databases - `PostgreSQL`:
```
CREATE TABLE users (
                      id serial PRIMARY KEY NOT NULL,
                      login TEXT NOT NULL UNIQUE,
                      password BIGINT NOT NULL,
                      api_key TEXT NOT NULL UNIQUE
)
```
```
CREATE TABLE point (
                      id serial PRIMARY KEY NOT NULL,
                      x DOUBLE PRECISION,
                      y DOUBLE PRECISION,
                      r DOUBLE PRECISION,
                      inside BOOLEAN,
                      owner_id INTEGER
)
```

[^1]: They all marked as `TODO` in files.
