package WheelChairUsers

class Users {
    String userName
    String password
    Date createdOn
    String apiAccessToken
    static constraints = {
        apiAccessToken nullable: true, maxSize: 255, unique: true
        userName(nullable: true)
        password(nullable: true)
        createdOn(nullable: true)
        apiAccessToken(nullable: true)        
    }
}
