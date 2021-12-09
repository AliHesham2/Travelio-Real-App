package com.example.bezo.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meals (val data:MealsList): Parcelable
@Parcelize
data class MealsList (val MealsList:List<MealsListData>): Parcelable
@Parcelize
data class MealsListData (val id:Int,val name:String): Parcelable