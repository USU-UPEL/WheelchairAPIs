# wheelchairAPIs

## Running

### Prerequisites

Make sure you have these installed on your machine

* [Node.js](https://nodejs.org/en/download/)
* [MongoDB](https://www.mongodb.com)
* **npm** This comes with Node.js, but make sure you check if you have it anyway

### Installing packages

Install packages

```
npm i
```

```
npm i -u nodemon
```

### Running the app

To run the app (dev. mode)

```
npm start
```

## Using

### Signup
`POST / - ContentType: application/json`
```
request body : {
  user: {
    email: string,
    password: string
  }
}
```
```
reply body: {
    user: {
        _id: STRING,
        email: STRING,
        token: STRING
    }
}
```

### Login
`POST /login - ContentType: application/json`
```
request body : {
  user: {
    email: string,
    password: string
  }
}
```
```
reply body: {
    user: {
        _id: STRING,
        email: STRING,
        token: STRING
    }
}
```

### Get Settings
`GET /settings - ContentType: application/json`
```
request header: {
    Authorization: "Token "+token
}
```
```
reply body: {
    settings: STRING
}
```

### Put Settings
`POST /settings - ContentType: application/json`
```
request header: {
    Authorization: "Token "+token
}
```
```
request body : {
  settings: STRING
}
```
```
reply body: {
    settings: STRING
}
```

### Get User
`GET /current - ContentType: application/json`
```
request header: {
    Authorization: "Token "+token
}
```
```
reply body: {
    user: {
        _id: STRING,
        email: STRING,
        token: STRING
    }
}
```

## Built With

* [Node.js](https://nodejs.org) - The backend framework used
* [Express.js](https://github.com/expressjs/express) - Node.js framework used
* [MongoDB](https://www.mongodb.com/) - Database platform used


## Authors

* **Antonio Erdeljac** - *Initial work* - [Passport-Tutorial](https://github.com/AntonioErdeljac/Blog-Tutorial)
* **Taylor Jones** - *Add settings* - [UPEL wheelchairAPIs]()
