package com.shop440.resp

import com.shop440.dao.models.User

/**
 * Created by mmumene on 03/11/2017.
 */
data class UserResponse(val user : User,
                        val message:String,
                        val token:String)