package emilien.tetu.taptogo.ui.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import emilien.tetu.taptogo.R
import emilien.tetu.taptogo.ui.filter.FilterActivity
import emilien.tetu.taptogo.ui.home.HomeActivity

class NavigationActivity : AppCompatActivity() {

    companion object{
        const val DEPARTURE_ADDRESS = "DEPARTURE_ADDRESS"
        const val ARRIVAL_ADDRESS = "ARRIVAL_ADDRESS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val departureAddressEditText = findViewById<EditText>(R.id.navAddressStart)
        val arrivalAddressEditText = findViewById<EditText>(R.id.navAddressStop)
        val searchButton = findViewById<MaterialButton>(R.id.navButton)

        searchButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(DEPARTURE_ADDRESS, departureAddressEditText.text.toString())
            intent.putExtra(ARRIVAL_ADDRESS, arrivalAddressEditText.text.toString())
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}