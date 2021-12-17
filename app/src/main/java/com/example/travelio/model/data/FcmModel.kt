package com.example.travelio.model.data


data class FcmModel(val to:String,val notification:FcmDetail,val data: FcmData)
data class FcmDetail(val body:String,val title:String)
data class FcmData(val name:String)
data class UpdateToken(val device_token:String)
data class SendNotificationPostRequest(val offer_id:Int,val offer_type:String)
