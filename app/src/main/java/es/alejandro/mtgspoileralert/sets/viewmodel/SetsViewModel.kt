package es.alejandro.mtgspoileralert.sets.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.usecase.IGetSetUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetsViewModel @Inject constructor(
    useCase: IGetSetUseCase
) : ViewModel() {

    private val _listOfSets: MutableState<List<Set>> = mutableStateOf(emptyList())
    val listOfSets: State<List<Set>> = _listOfSets

    init {
        viewModelScope.launch {
            val sets = useCase()
            _listOfSets.value = sets.data
        }
    }
}