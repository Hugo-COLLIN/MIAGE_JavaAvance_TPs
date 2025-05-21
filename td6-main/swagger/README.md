# Swagger

Ce répertoire contient un `package.json` qui permet de démarrer un serveur web local
afin de consulter [le swagger du projet](./webroot/random-coffee.yaml) en version html.

```bash
$ npm install
$ npm start
Starting up http-server, serving .
# http://127.0.0.1:8080/random-coffee.html
```

Si vous n'avez pas `npm` sur votre poste, le sous-répertoire `./exported`
contient une version html statique du swagger.
