package com.zhouz.aptter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


@MethodProcessor(name = "Method")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}