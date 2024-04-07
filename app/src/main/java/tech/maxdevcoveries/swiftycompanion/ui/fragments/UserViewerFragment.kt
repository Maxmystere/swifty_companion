package tech.maxdevcoveries.swiftycompanion.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.maxdevcoveries.swiftycompanion.R
import tech.maxdevcoveries.swiftycompanion.databinding.FragmentUserViewerBinding
import tech.maxdevcoveries.swiftycompanion.lib.FortyTwoApiClient
import tech.maxdevcoveries.swiftycompanion.lib.FortyTwoUser
import kotlin.math.abs

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

        CoroutineScope(Dispatchers.IO).launch {
            updateViewWithUser(
                FortyTwoApiClient().getApiService(requireContext())
                    .getUserInfo(args.userId)
            )
        }
        return binding.root
    }

    private fun updateViewWithUser(user: FortyTwoUser) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.textUserDisplayName.text = user.displayName
            binding.textUserEmail.text = user.email
            binding.textUserPoolDate.text = "${user.poolMonth} ${user.poolYear}"

            val xpPercent = (user.getActiveCursus()!!.level * 100).toInt() % 100

            binding.progressBarLevel.progress = xpPercent

            binding.textViewLevel.text = "${user.getActiveCursus()!!.level.toInt()}"
            binding.textViewXpPercent.text = "$xpPercent%"

            binding.viewSwitcherLoadGuard.displayedChild = 0 // 1
            Picasso.get()
                .load(user.image.link)
                .placeholder(R.drawable.forty_two_unknown)
                .into(binding.imageUserProfile)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}