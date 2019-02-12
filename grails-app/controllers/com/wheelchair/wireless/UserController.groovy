package com.wheelchair.wireless

import WheelChairUsers.Users
import grails.converters.JSON
import groovy.sql.Sql

class UserController {
    static allowedMethods =[signUp:"POST",signIn:"POST"]
    def dataSource
    def index() {
        header 'Access-Control-Allow-Origin', '*'
        def json = [status : 1, message : "Working Access"]
        render json as JSON
    }

    def signUp(){
        header 'Access-Control-Allow-Origin', '*'
        def json = [status : 1, message : ""]
        def db = new Sql(dataSource)
        try{
            println params
            def chkUsrExist = Users.findByUserName(params.userName)
            if(!chkUsrExist){
                def userObj = new Users()
                userObj.userName = params.userName
                userObj.userPref = ''
                userObj.password = params.password.digest('SHA-256')
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
                json = [status : 0, message : "User already exists"]
                render json as JSON
            }
        }catch(Exception ex){
            println "ex"+ex.printStackTrace()
            def exceptionJson = [status : 0, message : ex.localizedMessage]
            render exceptionJson as JSON
        }
    }

    def signIn(){
        header 'Access-Control-Allow-Origin', '*'
        println params
        def json = [status : 1, message : ""]
        def getUsr = Users.findByUserNameAndPassword(params.userName, params.password.digest('SHA-256'))
        if(getUsr){
            def blb = getUsr.userPref
            if(blb){
                byte[] bdata = blb.getBytes(1, (int)blb.length());
                String userPrefs = new String(bdata);
                json = [status : 1, message : "Login Successful",userPrefs:userPrefs, loginUser:params.userName]
                render json as JSON
            }else{
                json = [status : 1, message : "Login Successful", loginUser:params.userName]
                render json as JSON
            }
        }else{
            json = [status : 0, message : "Wrong Credentials"]
            render json as JSON
        }
    }

    def logout(){
        header 'Access-Control-Allow-Origin', '*'
        println params
        def getUsr = Users.findByUserName(params.userName)
        java.sql.Blob blob = org.hibernate.Hibernate.createBlob(params.userPrefs.getBytes());
        getUsr.userPref = blob
        getUsr.save()
        session.invalidate()
        def json = [status : 1, message : "Logout Successfully"]
        render json as JSON
    }

    def saveStatus(){
        header 'Access-Control-Allow-Origin', '*'
        def json = [status : 1, message : "Prefs Saved"]
        println params
        def getUsr = Users.findByUserName(params.userName)
        java.sql.Blob blob = org.hibernate.Hibernate.createBlob(params.userPrefs.getBytes());
        getUsr.userPref = blob
        getUsr.save()
        render json as JSON
    }

    def populateStatus(){
        header 'Access-Control-Allow-Origin', '*'
        println params
        def getUsr = Users.findByUserName(params.userName)

        def blb = getUsr.userPref
        byte[] bdata = blb.getBytes(1, (int)blb.length());
        String userPrefs = new String(bdata);
        def json = [status : 1, message : "Retrieving Prefs",userPrefs:userPrefs]
        render json as JSON

    }
}
