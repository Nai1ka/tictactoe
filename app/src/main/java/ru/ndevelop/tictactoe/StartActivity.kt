package ru.ndevelop.tictactoe

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*


class StartActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var modeDialog: Dialog
    private lateinit var idDialog: Dialog
    private lateinit var multModeDialog:Dialog
    private lateinit var hostButton: Button
    private lateinit var submitButton: Button
    private lateinit var idEditText: EditText
    private lateinit var guestButton: Button
    private lateinit var onOnePhoneButton:Button
    private lateinit var throughInternetButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //setSupportActionBar(toolbar)

        modeDialog = Dialog(this)
        idDialog = Dialog(this)
        multModeDialog=Dialog(this)
        modeDialog.setTitle("Выберите режим")
        modeDialog.setContentView(R.layout.dialog_choose_mode)
        idDialog.setContentView(R.layout.dialog_choose_id)
        multModeDialog.setContentView(R.layout.dialog_choose_mult_mode)
        hostButton = modeDialog.findViewById(R.id.btn_host)
        guestButton = modeDialog.findViewById(R.id.btn_guest)
        idEditText = idDialog.findViewById(R.id.et_dialog)
        submitButton = idDialog.findViewById(R.id.btn_submit)
        onOnePhoneButton=multModeDialog.findViewById(R.id.btn_on_one_phone)
        throughInternetButton=multModeDialog.findViewById(R.id.btn_through_internet)
        single_btn.setOnClickListener(this)
        multi_btn.setOnClickListener(this)
        hostButton.setOnClickListener(this)
        onOnePhoneButton.setOnClickListener(this)
        throughInternetButton.setOnClickListener(this)
        guestButton.setOnClickListener(this)
        submitButton.setOnClickListener(this)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        idDialog.window?.attributes = layoutParams
    }

    override fun onClick(v: View?) {
        when (v) {
            single_btn -> {
                val intent = Intent(this, SingleplayerActivity::class.java)
                startActivity(intent)
            }
            multi_btn -> {
                multModeDialog.show()
            }
            throughInternetButton ->{
                modeDialog.show()
            }
            onOnePhoneButton ->{
                val intent = Intent(this, MultiplayerActivity::class.java)
                intent.putExtra("multiplayerType",1)
                startActivity(intent)
            }
            hostButton -> {
                Utils.isHost=true
                val intent = Intent(this, MultiplayerActivity::class.java)
                intent.putExtra("multiplayerType",2)
                intent.putExtra("mySymbol","x")
                startActivity(intent)
            }
            guestButton -> {
                modeDialog.hide()
                idDialog.show()
            }
            submitButton ->{
                Utils.isHost=false
                val intent = Intent(this, MultiplayerActivity::class.java)
                intent.putExtra("roomId",idEditText.text.toString().toInt())
                intent.putExtra("multiplayerType",2)
                intent.putExtra("mySymbol","o")
                startActivity(intent)
            }
        }
    }

}
