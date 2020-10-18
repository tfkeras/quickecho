package jp.bellware.echo.actioncreator

import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import jp.bellware.echo.action.*
import jp.bellware.echo.repository.SettingRepository
import jp.bellware.echo.repository.SoundRepository
import jp.bellware.echo.util.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainActionCreatorTest {

    @ExperimentalCoroutinesApi
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK(relaxed = true)
    lateinit var dispatcher: Dispatcher

    @MockK(relaxed = true)
    lateinit var delayActionCreatorHelper: DelayActionCreatorHelper

    @MockK(relaxed = true)
    lateinit var settingRepository: SettingRepository

    @MockK(relaxed = true)
    lateinit var soundRepository: SoundRepository

    /**
     * テスト対象
     */
    private lateinit var actionCreator: MainActionCreator

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        MockKAnnotations.init(this)
        actionCreator = MainActionCreator(dispatcher, delayActionCreatorHelper, settingRepository, soundRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun onCreate() = runBlocking {
        every {
            settingRepository.isShowSoundMemoButton()
        } returns flow {
            emit(true)
        }
        actionCreator.onCreate().join()
        coVerifySequence {
            dispatcher.dispatch(MainSoundMemoButtonVisibilityAction(true))
        }
    }

    @Test
    fun onSoundLoadedFirstTime() = runBlocking {
        actionCreator.onSoundLoaded(false).join()
        verifySequence {
            dispatcher.dispatch(MainReadyAction)
        }
    }

    /**
     * 再生または停止状態でプロセスキルして、そこからの復帰ケース
     */
    @Test
    fun onSoundLoadedRestore() = runBlocking {
        actionCreator.onSoundLoaded(true).join()
        coVerifySequence {
            dispatcher.dispatch(MainReadyAction)
            dispatcher.dispatch(MainRestoreStartAction)
            soundRepository.restore()
            dispatcher.dispatch(MainRequestStopAction)
            dispatcher.dispatch(MainRestoreEndAction)
        }
    }

    @Test
    fun onRecordClick() = runBlocking {
        actionCreator.onRecordClick().join()
        coVerifySequence {
            dispatcher.dispatch(MainPreRecordAction)
            delayActionCreatorHelper.delay(500)
            dispatcher.dispatch(MainRecordAction)
        }
    }


    @Test
    fun onDeleteClick() = runBlocking {
        actionCreator.onDeleteClick().join()
        coVerifySequence {
            dispatcher.dispatch(MainDeleteAction)
            delayActionCreatorHelper.delay(200)
            dispatcher.dispatch(MainReadyAction)
        }
    }

    @Test
    fun onReplayClick() {
        actionCreator.onReplayClick()
        verifySequence {
            dispatcher.dispatch(MainRequestReplayAction)
        }
    }

    @Test
    fun onPlayClickIncludeSound() = runBlocking {
        actionCreator.onPlayClick(true).join()
        coVerifySequence {
            dispatcher.dispatch(MainPrePlayAction)
            delayActionCreatorHelper.delay(550)
            dispatcher.dispatch(MainPlayAction)
        }
    }

    @Test
    fun onPlayClickNoSound() = runBlocking {
        actionCreator.onPlayClick(false).join()
        coVerifySequence {
            dispatcher.dispatch(MainNoRecordAction)
            dispatcher.dispatch(MainDeleteAction)
            delayActionCreatorHelper.delay(200)
            dispatcher.dispatch(MainReadyAction)
        }
    }

    @Test
    fun onStopClick() {
        actionCreator.onStopClick()
        verifySequence {
            dispatcher.dispatch(MainRequestStopAction)
        }
    }

    @Test
    fun onMute() {
        actionCreator.onMute()
        verifySequence {
            dispatcher.dispatch(MainMuteAction)
        }
    }

    @Test
    fun onMaxRecordTimeOverIncludeSound() {
        actionCreator.onMaxRecordTimeOver(true)
        verifySequence {
            dispatcher.dispatch(MainMaxRecordTimeOverAction(true))
        }
    }

    @Test
    fun onMaxRecordTimeOverNoSound() {
        actionCreator.onMaxRecordTimeOver(false)
        verifySequence {
            dispatcher.dispatch(MainMaxRecordTimeOverAction(false))
        }
    }

    @Test
    fun soundMemo() {
        actionCreator.onSoundMemoClick()
        verifySequence {
            dispatcher.dispatch(MainSoundMemoAction)
        }
    }

    @Test
    fun onBackPressed() {
        actionCreator.onBackPressed()
        verifySequence {
            dispatcher.dispatch(MainBackPressedAction)
        }
    }

    @Test
    fun onPlayVisualVolumeUpdate() {
        actionCreator.onPlayVisualVolumeUpdate(1.0f)
        verifySequence {
            dispatcher.dispatch(MainPlayVisualVolumeUpdateAction(1.0f))
        }
    }

    @Test
    fun onPlayStart() {
        actionCreator.onPlayStart()
        verifySequence {
            dispatcher.dispatch(MainPlayStartAction)
        }
    }

    @Test
    fun onPlayEnd() {
        actionCreator.onPlayEnd()
        verifySequence {
            dispatcher.dispatch(MainPlayEndAction)
        }
    }
}
