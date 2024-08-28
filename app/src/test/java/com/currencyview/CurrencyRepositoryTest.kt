package com.currencyview

import com.currencyview.common.util.testing.MockIdlingResourceHelper
import com.currencyview.data.network.CurrencyDataSource
import com.currencyview.data.repository.CurrencyRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyRepositoryTest {

    @Mock
    lateinit var mCurrencyDataSourceMock: CurrencyDataSource

    @Mock
    val mockIdlingResourceHelper = MockIdlingResourceHelper()

    @Test
    fun testIfCurrencySymbolsReturns() = runTest {
        // Arrange
        `when`(mCurrencyDataSourceMock.fetchCurrenciesSymbols())
            .thenReturn(CurrencyTestMock.currencySymbolMockSuccess)

        val expected = CurrencyTestMock.symbolResponseMockExist

        val currencyRepository = CurrencyRepository(mCurrencyDataSourceMock, mockIdlingResourceHelper)

        // Act
        val result = currencyRepository.getCurrencySymbol()

        // Assert
        Assert.assertEquals(expected, result)
    }

    @Test
    fun testIfCurrencySymbolsReturnsNull() = runTest {
        `when`(mCurrencyDataSourceMock.fetchCurrenciesSymbols())
            .thenReturn(CurrencyTestMock.currencySymbolMockSuccessNull)

        val expected = CurrencyTestMock.symbolResponseMockNull

        val currencyRepository = CurrencyRepository(mCurrencyDataSourceMock, mockIdlingResourceHelper)

        val result = currencyRepository.getCurrencySymbol()

        Assert.assertEquals(expected, result)
    }

    @Test
    fun testIfCurrencySymbolsReturnsException() = runTest {
        `when`(mCurrencyDataSourceMock.fetchCurrenciesSymbols())
            .thenThrow(RuntimeException("Error message"))

        val expected = CurrencyTestMock.symbolResponseMockNotExist

        val currencyRepository = CurrencyRepository(mCurrencyDataSourceMock, mockIdlingResourceHelper)

        val result = currencyRepository.getCurrencySymbol()

        Assert.assertEquals(expected, result)
    }

    @Test
    fun testIfCurrencyRatesReturns() = runTest {
        `when`(mCurrencyDataSourceMock.fetchCurrencyRates("USD"))
            .thenReturn(CurrencyTestMock.currencyRatesMockSuccess)

        val expected = CurrencyTestMock.rateResponseMockExist

        val currencyRepository = CurrencyRepository(mCurrencyDataSourceMock, mockIdlingResourceHelper)

        val result = currencyRepository.getCurrencyRate("USD")

        Assert.assertEquals(expected, result)
    }

    @Test
    fun testIfCurrencyRatesReturnsNull() = runTest {
        `when`(mCurrencyDataSourceMock.fetchCurrencyRates("USD"))
            .thenReturn(CurrencyTestMock.currencyRatesMockSuccessNull)

        val expected = CurrencyTestMock.rateResponseMockNull

        val currencyRepository = CurrencyRepository(mCurrencyDataSourceMock, mockIdlingResourceHelper)

        val result = currencyRepository.getCurrencyRate("USD")

        Assert.assertEquals(expected, result)
    }

    @Test
    fun testIfCurrencyRatesReturnsException() = runTest {
        `when`(mCurrencyDataSourceMock.fetchCurrencyRates("USD"))
            .thenThrow(RuntimeException("Error message"))

        val expected = CurrencyTestMock.rateResponseMockNotExist

        val currencyRepository = CurrencyRepository(mCurrencyDataSourceMock, mockIdlingResourceHelper)

        val result = currencyRepository.getCurrencyRate("USD")

        Assert.assertEquals(expected, result)
    }
}
