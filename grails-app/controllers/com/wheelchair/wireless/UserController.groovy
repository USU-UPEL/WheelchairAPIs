package com.wheelchair.wireless

import WheelChairUsers.Users
import grails.converters.JSON
import groovy.sql.Sql

class UserController {
    static allowedMethods =[signUp:"POST",signIn:"POST"]
    def dataSource
    def index() { 
        def json = [status : 1, message : "Working Access"]
        render json as JSON
    }

    def signUp(){
        def json = [status : 1, message : ""]
        def db = new Sql(dataSource)
        try{
            println params
            def chkUsrExist = Users.findByUserName(params.userName)
            if(!chkUsrExist){
                def userObj = new Users()
                userObj.userName = params.userName
                userObj.password = params.password
                userObj.createdOn = new Date()
                def apiAccessToken = UUID.randomUUID().toString()+new Date().getTime()
                def tokenAfterOpr = apiAccessToken.split("-").join()
                userObj.apiAccessToken = tokenAfterOpr
                if(userObj.save(flush:true,failOnError: true)){
                    session['user'] = params.userName
                    json = [status : 1, message : "User Sucessfully Regsitered", data: params.userName]
                    render json as JSON
                }else{
                    valUser.errors.allErrors.each{
                        println it 
                    }
                }
            }
            else{
                json = [status : 0, message : "User already Exist"]
                render json as JSON
            }
        }catch(Exception ex){
            println "ex"+ex.printStackTrace()
            def exceptionJson = [status : 0, message : ex.localizedMessage]
            render exceptionJson as JSON
        }
    }

    def signIn(){
        println params
        def json = [status : 1, message : ""]
        def getUsr = Users.findByUserNameAndPassword(params.loguserName, params.logpassword)
        println "getUsr"+ getUsr
//        def userPreferences = []
        if(getUsr){
//            if(getUsr.userPref>0){
//                println getUsr.userPref.getClass().getName() 
//                userPreferences = String(getUsr.userPref)
//            }
            json = [status : 1, message : "Login Successful",userPrefs:getUsr.userPref, loginUser:params.loguserName]
            render json as JSON
        }else{
            json = [status : 0, message : "Wrong Credentials"]
            render json as JSON
        }
    }
    
    def logout(){
        println params
        print params.arr
        def getUsr = Users.findByUserName(params.userName)
        print "getUsr"+getUsr
        getUsr.userPref = params.arr
        getUsr.save()
        session.invalidate()
        def json = [status : 1, message : "Logout Successfully"]
            render json as JSON
    }

}