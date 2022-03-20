package fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentAddAlarmBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Alarm
import viewModel.AlarmListViewModel

@Suppress("UNCHECKED_CAST")
class AddAlarmFragment : Fragment() {

    private lateinit var binding: FragmentAddAlarmBinding

    private val alarmListViewModel: AlarmListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                activity?.application?.let { AlarmListViewModel(it) } as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtSave.setOnClickListener { //tao 1 bao thuc moi
            createAlarm()
            Navigation.findNavController(view)
                .navigate(R.id.action_addAlarmFragment_to_alarmFragment)
        }
        binding.checkRecur.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                binding.recurLayout.visibility = View.VISIBLE
                binding.btnDoneRecur.setOnClickListener {
                    with(binding) {
                        if (!this.checkMon.isChecked && !this.checkTue.isChecked && !this.checkWed.isChecked && !this.checkThu.isChecked && !this.checkFri.isChecked && !this.checkSat.isChecked && !this.checkSun.isChecked) {
                            this.checkRecur.isChecked = false
                        }
                    }
                    binding.recurLayout.visibility = View.GONE
                    binding.txtSave.visibility = View.VISIBLE
                    binding.txtCancel.visibility = View.VISIBLE
                }
                binding.txtSave.visibility = View.GONE
                binding.txtCancel.visibility = View.GONE
            } else {
                binding.recurLayout.visibility = View.GONE

            }
        }
        binding.txtCancel.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_addAlarmFragment_to_alarmFragment)

        }
    }

    private fun createAlarm() {
        val alarm = Alarm(
            binding.timePicker.hour,
            binding.timePicker.minute,
            binding.checkRecur.isChecked,
            binding.checkMon.isChecked,
            binding.checkTue.isChecked,
            binding.checkWed.isChecked,
            binding.checkThu.isChecked,
            binding.checkFri.isChecked,
            binding.checkSat.isChecked,
            binding.checkSun.isChecked,
            binding.edtAlarmLabel.text.toString(),
            true,
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
        lifecycleScope.launch(Dispatchers.IO) {
            alarmListViewModel.insert(alarm)
        }

        Log.e("Random ID", alarm.id.toString())
        val hour: Int = binding.timePicker.hour
        val minute: Int = binding.timePicker.minute
        Toast.makeText(requireActivity(), "$hour : $minute", Toast.LENGTH_LONG).show()
        activity?.let { alarm.schedule(it as Context) }
        Log.e("Add alarm context", activity.toString())

    }
}