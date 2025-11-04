// the credentials provided in docker-compose.yml file are used only for admin collection
// and not for the newly created collection
db.createUser(
    {
        user: "user",
        pwd: "secret",
        roles: [
            {
                role: "readWrite",
                db: "benchmark_db"
            }
        ]
    }
);