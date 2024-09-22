package xyz.katiedotson.deweydecimal.book

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookInputRepository @Inject constructor() {

    private var _bookModel: BookModel? = null

    fun saveBookResult(bookModel: BookModel) {
        _bookModel = bookModel
    }

    fun getBookResult(): BookModel? {
        return _bookModel
    }
}
