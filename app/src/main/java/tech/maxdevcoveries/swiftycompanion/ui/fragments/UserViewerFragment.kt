package tech.maxdevcoveries.swiftycompanion.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.maxdevcoveries.swiftycompanion.databinding.FragmentUserViewerBinding
import tech.maxdevcoveries.swiftycompanion.lib.FortyTwoApiClient

class UserViewerFragment : Fragment() {

    private var _binding: FragmentUserViewerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: UserViewerFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserViewerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textUserDisplay

        CoroutineScope(Dispatchers.IO).launch {
            textView.text =
                FortyTwoApiClient().getApiService(requireContext())
                    .getUserInfo(args.userId).displayName
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}