package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.alarmapp.databinding.ActivityAlarmRingBinding
import model.Alarm
import service.AlarmService
import java.util.*

class AlarmRingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmRingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(applicationContext, AlarmService::class.java)

        initTimeAndLabel()

        // onClick
        binding.btnStop.setOnClickListener { // if stop, stop service => stop Alarm
            //val intent = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intent)
            finish() // tat activity
        }
        binding.btnSnooze.setOnClickListener {
            // khi snooze, alarm se tat vao luc do va lap lai bao thuc sau 10p, => tao 1 bao thuc moi sau 10p
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, 10)

            val newAlarm = Alarm(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    recurring = true,
                    monday = false,
                    tuesday = false,
                    wednesday = false,
                    thursday = false,
                    friday = false,
                    saturday = false,
                    sunday = false,
                    label = "Snooze",
                    isOn = true,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
            )
            newAlarm.schedule(applicationContext) // tao 1 bao thuc moi vao 10 phut sau

            // ket thuc activity ring
            applicationContext.stopService(intent)
            finish()

        }

    }

    private fun initTimeAndLabel() {
        val alarmHour = intent.getIntExtra("HOUR",0)
        val alarmMinute = intent.getIntExtra("MINUTE",0)

        Log.e("Hour Ring", intent.getIntExtra("HOUR",0).toString())
        Log.e("Minute Ring", intent.getIntExtra("MINUTE",0).toString())

        val alarmTime = "$alarmHour : $alarmMinute"
        binding.txtALarmTime.text = alarmTime

        intent.getStringExtra("LABEL")?.let { Log.e("Label ring", it) }
        val alarmLabel = intent.getStringExtra("LABEL")

        binding.txtAlarmLabel.text = alarmLabel
    }
}