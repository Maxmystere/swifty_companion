package tech.maxdevcoveries.swiftycompanion.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
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

            try {
                val obj = FortyTwoApiClient().getApiService(requireContext())
                    .getUserInfo(args.userId)

                CoroutineScope(Dispatchers.Main).launch {
                    if (obj.isSuccessful && obj.body() != null) {
                        updateViewWithUser(obj.body()!!)
                    } else {
                        val jsonError = obj.errorBody()?.string()?.let { JSONObject(it) }
                        val errorText = jsonError?.optString("error")
                        val errorDescText = jsonError?.optString("error_description")

                        if (errorText?.isNotEmpty() == true)
                            binding.textErrorCode.text = errorText
                        else
                            binding.textErrorCode.text = obj.code().toString()
                        if (errorDescText?.isNotEmpty() == true)
                            binding.textErrorMessage.text = errorDescText
                        else
                            binding.textErrorMessage.text = obj.errorBody()?.string()

                        binding.viewSwitcherLoadGuard.displayedChild = 2
                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    val errorText = "No internet"
                    val errorDescText = "Please check your internet connection"

                    binding.textErrorCode.text = errorText
                    binding.textErrorMessage.text = errorDescText

                    binding.viewSwitcherLoadGuard.displayedChild = 2
                }
            }

        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
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
