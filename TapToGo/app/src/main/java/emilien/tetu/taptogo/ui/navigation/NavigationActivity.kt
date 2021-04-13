package emilien.tetu.taptogo.ui.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import emilien.tetu.taptogo.R
import emilien.tetu.taptogo.databinding.ActivityHomeBinding
import emilien.tetu.taptogo.databinding.ActivityNavigationBinding
import emilien.tetu.taptogo.ui.filter.FilterActivity
import emilien.tetu.taptogo.ui.home.HomeActivity

class NavigationActivity : AppCompatActivity() {

    companion object{
        const val DEPARTURE_ADDRESS = "DEPARTURE_ADDRESS"
        const val ARRIVAL_ADDRESS = "ARRIVAL_ADDRESS"
    }

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_navigation)

        binding.navButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(DEPARTURE_ADDRESS, binding.navAddressStart.text.toString())
            intent.putExtra(ARRIVAL_ADDRESS, binding.navAddressStop.text.toString())
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}