# Lovely Backend

This is backend of the Doran House project. 

To start the backend, at the root directory, run command:

```shell
> docker-compose up -d
```

Check out `docker-compose.yml` for more information.



To check if containers run correctly, try calling an API

```shell
curl --location --request GET 'http://localhost:9090' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWNoZWx2ZWxhc3F1ZXoxMTY2QGhvdG1haWwuY2EiLCJST0xFX1NFTExFUiI6dHJ1ZSwiZXhwIjoxNjQxMzE3Nzk0LCJpYXQiOjE2NDEzMTc3MzR9.Jz7bFDl1spzQrk2WiiZVx9m95afNILfofAkCpfqrA6h2arsQy1a7g5CFuzxVltzO9IHFV2Mlg7cfD0ebKtlptg'
```

```shell
{
    "message": "Hi, I'm Doran Backend!"
}
```



Check out `src/main/java/controller` for more endpoints.

If your DB has no data, check out `src/main/resources` and execute them in ascending order (make sure you have connected to MySQL first). 