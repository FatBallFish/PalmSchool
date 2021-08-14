package com.fatballfish.palmschool.ui.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.fatballfish.palmschool.PalmSchoolApplication
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.ui.realAuth.RealAuthCreateFragment
import com.fatballfish.palmschool.ui.school.SchoolChoosenFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_user_info_edit.*

@AndroidEntryPoint
class UserInfoEditActivity : AppCompatActivity() {
    private var key: String? = null
    private var value: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_edit)
        key = intent.getStringExtra("key")
        value = intent.getStringExtra("value")
        Log.d("InfoEdit", "$key:$value")
        if (key.isNullOrEmpty()) {
            Toast.makeText(PalmSchoolApplication.context, "key值为空", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        when (key) {
            "nickname", "email", "dept", "major", "cls" -> {
                val fragment = NormalUserInfoEditFragment.newInstance(key!!, value ?: "")
                val beginTransaction = supportFragmentManager.beginTransaction()
                beginTransaction.add(R.id.fragment_edit, fragment)
                beginTransaction.commit()
            }
            "school" -> {
                val fragment = SchoolChoosenFragment.newInstance(key!!, value ?: "")
                val beginTransaction = supportFragmentManager.beginTransaction()
                beginTransaction.add(R.id.fragment_edit, fragment)
                beginTransaction.commit()
            }
            "noRealAuth" -> {
                val fragment = RealAuthCreateFragment.newInstance(key!!, value ?: "")
                val beginTransaction = supportFragmentManager.beginTransaction()
                beginTransaction.add(R.id.fragment_edit, fragment)
                beginTransaction.commit()
            }
        }
    }

}