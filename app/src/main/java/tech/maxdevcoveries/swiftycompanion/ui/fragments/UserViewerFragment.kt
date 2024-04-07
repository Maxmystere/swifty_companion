package tech.maxdevcoveries.swiftycompanion.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.maxdevcoveries.swiftycompanion.R
import tech.maxdevcoveries.swiftycompanion.databinding.FragmentUserViewerBinding
import tech.maxdevcoveries.swiftycompanion.lib.FortyTwoApiClient
import tech.maxdevcoveries.swiftycompanion.lib.FortyTwoUser
import tech.maxdevcoveries.swiftycompanion.ui.adapter.UserProjectAdapter

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

        binding.viewSwitcherLoadGuard.displayedChild = 0

        CoroutineScope(Dispatchers.IO).launch {

            val obj = FortyTwoApiClient().getApiService(requireContext())
                    .getUserInfo(args.userId)

            CoroutineScope(Dispatchers.Main).launch {
                if (obj.isSuccessful && obj.body() != null) {
                    updateViewWithUser(obj.body()!!)
                } else {
                    Toast.makeText(context, obj.message(), Toast.LENGTH_LONG).show()
                    binding.textErrorCode.text = obj.code().toString()
                    binding.textErrorMessage.text = obj.errorBody()?.string()

                    binding.viewSwitcherLoadGuard.displayedChild = 2
                }
            }

        }
        return binding.root
    }

    private fun updateViewWithUser(user: FortyTwoUser) {
        binding.textUserDisplayName.text = user.displayName
        binding.textUserEmail.text = user.email
        binding.textUserPoolDate.text = "${user.poolMonth} ${user.poolYear}"

        val xpPercent = (user.getActiveCursus().level * 100).toInt() % 100

        binding.progressBarLevel.progress = xpPercent
        binding.textViewXpPercent.text = "$xpPercent%"

        binding.textViewLevel.text = "${user.getActiveCursus().level.toInt()}"

        Picasso.get()
            .load(user.image.link)
            .placeholder(R.drawable.forty_two_unknown)
            .into(binding.imageUserProfile)

        binding.recyclerViewProjects.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerViewProjects.adapter = UserProjectAdapter(user.getCursusProjects())
        binding.recyclerViewProjects.setHasFixedSize(true)

        binding.viewSwitcherLoadGuard.displayedChild = 1 // 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
