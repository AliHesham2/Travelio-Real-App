package com.example.travelio.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Levels (val data:LevelsList): Parcelable
@Parcelize
data class LevelsList (val TransportLevelsList:List<LevelsListData>): Parcelable
@Parcelize
data class LevelsListData (val id:Int, val name:String): Parcelable
