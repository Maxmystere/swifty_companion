package tech.maxdevcoveries.swiftycompanion.ui.transform

import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tech.maxdevcoveries.swiftycompanion.FortyTwoSearchRecentSuggestions
import tech.maxdevcoveries.swiftycompanion.R
import tech.maxdevcoveries.swiftycompanion.databinding.FragmentSearchUserBinding

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
class SearchUserFragment : Fragment() {

    private var _binding: FragmentSearchUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.searchUserSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                SearchRecentSuggestions(
                    context,
                    FortyTwoSearchRecentSuggestions.AUTHORITY,
                    FortyTwoSearchRecentSuggestions.MODE
                ).saveRecentQuery(query, null)

                if (query != null) {

                    val act = SearchUserFragmentDirections.toUserViewer(query)

                    view!!.findNavController()
                        .navigate(act)
                }
                // a toast message as no  data found..
                Toast.makeText(context, query, Toast.LENGTH_LONG)
                    .show()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                //listAdapter.filter.filter(newText)
                return false
            }
        })
        binding.searchUserSearchView.setOnSearchClickListener {
            Log.d("MaxLog", binding.searchUserSearchView.query.toString())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}