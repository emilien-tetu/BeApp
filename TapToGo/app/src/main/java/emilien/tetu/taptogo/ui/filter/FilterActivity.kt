package emilien.tetu.taptogo.ui.filter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import emilien.tetu.taptogo.R
import emilien.tetu.taptogo.databinding.ActivityFilterBinding
import emilien.tetu.taptogo.databinding.ActivityNavigationBinding
import emilien.tetu.taptogo.ui.home.HomeActivity

class FilterActivity : AppCompatActivity() {

    companion object{
        const val NAME_STATION = "NAME_STATION"
        const val BIKE_AVAILABLE = "BIKE_AVAILABLE"
        const val ONLY_OPEN = "ONLY_OPEN"
    }

    private lateinit var binding: ActivityFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_filter)

        var isCheck = false

        binding.filterSwitch.setOnCheckedChangeListener { _, isChecked -> isCheck = isChecked }

        binding.filterButton.setOnClickListener {

            val numberBikeString : String = binding.filterBikeAvailable.text.toString()
            val numberBike : Int = if (numberBikeString != ""){numberBikeString.toInt()} else {0}

            val nameStation : String = binding.filterNameStation.text.toString()

            val intent = Intent(this,HomeActivity::class.java)
            intent.putExtra(NAME_STATION, nameStation)
            intent.putExtra(BIKE_AVAILABLE, numberBike)
            intent.putExtra(ONLY_OPEN, isCheck)
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}