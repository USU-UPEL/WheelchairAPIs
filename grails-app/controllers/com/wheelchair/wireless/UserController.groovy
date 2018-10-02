package com.wheelchair.wireless

import WheelChairUsers.Users
import grails.converters.JSON
import groovy.sql.Sql

class UserController {
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
                    json = [status : 1, message : "User Sucessfully Regsitered"]
                    render json as JSON
                }else{
                    valUser.errors.allErrors.each{
                        println it 
                    }
                }
            }
            else{
                json = [status : 1, message : "User already Exist"]
                render json as JSON
            }
        }catch(Exception ex){
            println "ex"+ex.printStackTrace()
            def exceptionJson = [status : 0, message : ex.localizedMessage]
            render exceptionJson as JSON
        }
    }

    def signIn(){
        def json = [status : 1, message : ""]
        def getUsr = Users.findByUserNameAndPassword(params.userName, params.password)
        if(getUsr){
            json = [status : 1, message : "Login Successful"]
            render json as JSON
        }else{
            json = [status : 0, message : "Wrong Credentials"]
            render json as JSON
        }
    }

}