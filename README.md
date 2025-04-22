# Requerimientos bonus

**US 0012:** Obtener un listado de todos los productos en promoción de un determinado vendedor

| Method  | Sign |
| ------------- |:-------------:|
| GET    | /products/promo-post/list?user_id={userId}    |

**Response:**     
```json
{ 
    "user_id": 234,
    "user_name": "vendedor1",
    "posts": [
        {
            "user_id": 234,
            "post_id": 18,
            "date": "29-04-2021",
            "product": {
                "product_id": 1,
                "product_name": "Silla Gamer",
                "type": "Gamer",
                "brand": "Racer",
                "color": "Red & Black",
                "notes": "Special Edition"
            },
            "category": "100",
            "price": 15000.50,
            "has_promo": true,
            "discount": 0.25
        }
    ]
}
```
**Filtros/Parámetros:**

| Parámetro    | Tipo       | Descripción / Ejemplo                                                                                   |
|--------------|------------|---------------------------------------------------------------------------------------------------------|
| user_id      | int        | Número que identifica a cada usuario                                                                    |
| user_name    | String     | Cadena de caracteres que representa el nombre del usuario                                               |
| post_id      | int        | Número identificatorio de cada una de las publicaciones                                                 |
| date         | LocalDate  | Fecha de la publicación en formato dd-MM-yyyy                                                           |
| product_id   | int        | Número identificatorio de un producto asociado a una publicación                                         |
| product_name | String     | Cadena de caracteres que representa el nombre de un producto                                             |
| type         | String     | Cadena de caracteres que representa el tipo de un producto                                              |
| brand        | String     | Cadena de caracteres que representa la marca de un producto                                              |
| color        | String     | Cadena de caracteres que representa el color de un producto                                              |
| notes        | String     | Cadena de caracteres para colocar notas u observaciones de un producto                                   |
| category     | int        | Identificador que sirve para conocer la categoría a la que pertenece un producto. Ej: 100: Sillas, 58: Teclados  |
| price        | double     | Precio del producto                                                                                      |
| has_promo    | boolean    | Campo true o false para determinar si un producto está en promoción o no                                  |
| discount     | double     | En caso de que un producto estuviese en promoción, establece el monto de descuento                         |

---

**US 0013:** Obtener un listado de las publicaciones realizadas por los vendedores que un usuario sigue cuyo producto contenga palabra clave.

| Method  | Sign |
| ------------- |:-------------:|
| GET    | /products/followed/{userId}/filterByKeyword?filter=keyword   |

**Response:**     
```json
{
    "user_id": 4698,
    "posts": [ {
            "user_id": 234,
            "post_id": 32,
            "date": "01-05-2021",
            "product": {
                "product_id": 62,
                "product_name": "Headset RGB Inalámbrico",
                "type": "Gamer",
                "brand": "Razer",
                "color": "Green with RGB",
                "notes": "Sin Batería"
            },
            "category": 120,
            "price": 2800.69
        },
        {
            "user_id": 234,
            "post_id": 18,
            "date": "29-04-2021",
            "product": {
                "product_id": 1,
                "productName": "Silla Gamer",
                "type": "Gamer",
                "brand": "Racer",
                "color": "Red & Black",
                "notes": "Special Edition"
            },
            "category": 100,
            "price": 15000.50
        }
    ]
}
```

**Filtros/Parámetros:**

| Parámetro    | Tipo       | Descripción / Ejemplo                                                                                   |
|--------------|------------|---------------------------------------------------------------------------------------------------------|
| user_id      | int        | Número que identifica a cada usuario                                                                    |

---

**US 0014:** Obtener un listado de las publicaciones realizadas por los vendedores que un usuario sigue filtradas por categoría.

| Method  | Sign |
| ------------- |:-------------:|
| GET    | /products/followed/{userId}/filterByCategory?filter=category   |

**Response:**     
```json
{
    "user_id": 4698,
    "posts": [ {
            "user_id": 234,
            "post_id": 32,
            "date": "01-05-2021",
            "product": {
                "product_id": 62,
                "product_name": "Headset RGB Inalámbrico",
                "type": "Gamer",
                "brand": "Razer",
                "color": "Green with RGB",
                "notes": "Sin Batería"
            },
            "category": 120,
            "price": 2800.69
        },
        {
            "user_id": 234,
            "post_id": 18,
            "date": "29-04-2021",
            "product": {
                "product_id": 1,
                "productName": "Silla Gamer",
                "type": "Gamer",
                "brand": "Racer",
                "color": "Red & Black",
                "notes": "Special Edition"
            },
            "category": 100,
            "price": 15000.50
        }
    ]
}
```

**Filtros/Parámetros:**

| Parámetro    | Tipo       | Descripción / Ejemplo                                                                                   |
|--------------|------------|---------------------------------------------------------------------------------------------------------|
| user_id      | int        | Número que identifica a cada usuario                                                                    |

---

**US 0015:**  Obtener un listado de todos los productos en promoción de todos los vendedores (útil para un “explorar promociones”)

| Method  | Sign |
| ------------- |:-------------:|
| GET    | /products/promotions   |

**Response:**     
```json

```
