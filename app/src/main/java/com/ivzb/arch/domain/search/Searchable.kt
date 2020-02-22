package com.ivzb.arch.domain.search

import com.ivzb.arch.model.Link

/**
 * Sealed class that represents searchable contents.
 */
sealed class Searchable {

    class SearchedLink(val link: Link) : Searchable()
}
