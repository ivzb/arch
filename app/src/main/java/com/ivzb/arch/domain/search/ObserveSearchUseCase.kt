package com.ivzb.arch.domain.search

import androidx.lifecycle.Transformations
import com.ivzb.arch.data.links.LinksRepository
import com.ivzb.arch.domain.MediatorUseCase
import com.ivzb.arch.domain.Result
import javax.inject.Inject

/**
 * Performs a search from a query string.
 *
 * A session is returned in the results if the title, description, or tag matches the query parameter.
 */
class ObserveSearchUseCase @Inject constructor(
    private val repository: LinksRepository
) : MediatorUseCase<String, List<Searchable>>() {

    override fun execute(parameters: String) {
        val query = parameters.toLowerCase()

        val observeSearchedLinks = Transformations.map(
            repository.observeAll()
        ) {
            it
                .toSet()
                .filter { link ->
                    query.isEmpty() ||
                            link.title?.toLowerCase()?.contains(query) ?: false ||
                            link.sitename?.toLowerCase()?.contains(query) ?: false ||
                            link.url.toLowerCase().contains(query)
                }
                .map { Searchable.SearchedLink(it) }
        }

        result.addSource(observeSearchedLinks) {
            result.postValue(Result.Success(it))
        }
    }
}
