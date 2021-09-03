package com.puntogris.posture.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.puntogris.posture.R
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class Reminder(

    @PrimaryKey(autoGenerate = false)
    var reminderId: String = "",

    @ColumnInfo
    var uid: String = "",

    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var timeInterval: Int = 0,

    @ColumnInfo
    var startTime: Int = -1,

    @ColumnInfo
    var endTime: Int = -1,

    @ColumnInfo
    var alarmDays: List<Int> = listOf(),

    @ColumnInfo
    var color: Int = R.color.grey,

    @ColumnInfo
    var vibrationPattern: Int = 0,

    @ColumnInfo
    var soundUri: String = "",

    @ColumnInfo
    var soundName: String = ""
):Parcelable{

    private fun isAlarmPastMidnight() = startTime > endTime

    fun isAlarmInRange(minutesSinceMidnight: Int): Boolean{
        return if (isAlarmPastMidnight()) minutesSinceMidnight !in (endTime..startTime)
        else minutesSinceMidnight in (startTime..endTime)
    }

    fun isAlarmPastMidnightAndInRange(minutesSinceMidnight: Int) =
        isAlarmPastMidnight() && isAlarmInRange(minutesSinceMidnight)

    fun alarmDaysSummary(daysList: Array<String>) =
         alarmDays.joinToString(", ") {
            daysList[it].first().toString()
         }

    fun timeIntervalSummary(): String{
        return if (timeInterval < 60){
            "$timeInterval m."
        }else{
            "${timeInterval/60} h. ${timeInterval % 60} m."
        }
    }

    fun requiredInfoFilled(): Boolean{
        return (name.isNotBlank() &&
                timeInterval != 0 &&
                startTime != -1 &&
                endTime != -1 &&
                alarmDays.isNotEmpty()
                )
    }
}