package com.example.chatapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.hbb20.CountryCodePicker


class MainActivity : AppCompatActivity() {

    var countryCodePicker: CountryCodePicker? = null
    var phone: EditText? = null
    var message:EditText? = null
    var whats_btn: Button? = null
    var telegram_btn: Button? = null
    var viber_btn: Button? = null
    var messagestr: String? = null
    var phonestr:String= ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        countryCodePicker = findViewById(R.id.countryCode)
        phone = findViewById(R.id.phoneNo)
        message = findViewById(R.id.message)
        whats_btn = findViewById(R.id.whats_btn)
        telegram_btn=findViewById(R.id.telegram_btn);
        viber_btn=findViewById(R.id.viber_btn);

        whats_btn!!.setOnClickListener {
            messagestr = message!!.text.toString();
            phonestr = phone!!.text.toString();

            if (messagestr!!.isNotEmpty() && phonestr.isNotEmpty()) {

                countryCodePicker!!.registerCarrierNumberEditText(phone);
                phonestr = countryCodePicker!!.fullNumber;

                if (isWhatappInstalled()){
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phonestr+
                            "&text="+messagestr));
                    startActivity(i);
                    message!!.setText("");
                    phone!!.setText("");

                }else {
                    Toast.makeText(this,"Whatsapp is not installed",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in the Phone no. and message it can't be empty", Toast.LENGTH_LONG).show();
            }
        }

        telegram_btn!!.setOnClickListener {
            if (isTelegramInstalled()) {
                val appName = "org.telegram.messenger"
                val myIntent = Intent(Intent.ACTION_SEND)
                myIntent.type = "text/plain"
                myIntent.setPackage(appName)
                myIntent.putExtra(Intent.EXTRA_TEXT, "msg") //
                startActivity(Intent.createChooser(myIntent, "Share with"))
            }
            else
            {
                Toast.makeText(this,"Telegram is not installed",Toast.LENGTH_SHORT).show()
            }

        }

        viber_btn!!.setOnClickListener {
            messagestr = message!!.text.toString()
            phonestr = phone!!.text.toString()
            if (messagestr!!.isNotEmpty() && phonestr.isNotEmpty()) {
                countryCodePicker!!.registerCarrierNumberEditText(phone)
                phonestr = countryCodePicker!!.fullNumber
                if(isViberInstalled()) {
                    val smsIntent = Intent(Intent.ACTION_SENDTO)
                    smsIntent.addCategory(Intent.CATEGORY_DEFAULT)
                    smsIntent.setPackage("com.viber.voip")
                    smsIntent.data = Uri.parse("sms:"+phonestr)
                    smsIntent.putExtra("address", phonestr)
                    smsIntent.putExtra("sms_body", "hello!")
                    startActivity(smsIntent)
                }
                else
                {
                    Toast.makeText(this,"Viber is not installed",Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this, "Please fill in the Phone no. and message it can't be empty", Toast.LENGTH_LONG).show();
            }
        }

    }

    private fun isWhatappInstalled(): Boolean {
        val packageManager = packageManager
        val whatsappInstalled: Boolean
        whatsappInstalled = try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return whatsappInstalled
    }

    private fun isTelegramInstalled(): Boolean {
        val packageManager = packageManager
        val telegramInstalled: Boolean
        telegramInstalled = try {
            packageManager.getPackageInfo("org.telegram.messenger", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return telegramInstalled
    }

    private fun isViberInstalled(): Boolean {
        val packageManager = packageManager
        val viberInstalled: Boolean
        viberInstalled = try {
            packageManager.getPackageInfo("com.viber.voip", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return viberInstalled
    }
}