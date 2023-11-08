
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import androidx.compose.foundation.layout.height
// import androidx.compose.material.Button
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.rememberCameraPositionState

@Preview
@Composable
fun previewSetting() {
    Map(0.1, 0.1, 0.1, 0.1)
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun Map(CurLng: Double, CurLat: Double, MarkerLng: Double, MarkerLat: Double) {
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 20.0, minZoom = 5.0)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = true)
        )
    }
    // 현재 위치 설정
    val currentLocation = LatLng(33.532600, 127.024612)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(currentLocation, 20.0)
    }
//    Box(Modifier.fillMaxSize()) {
    Box(Modifier.height(300.dp)) {
        NaverMap(properties = mapProperties, uiSettings = mapUiSettings) {
            // 마커 찍는 법.
            Marker(
                state = MarkerState(position = LatLng(37.532600, 127.024612)),
                captionText = "Marker in Seoul"
            )
            Marker(
                state = MarkerState(position = LatLng(37.390791, 127.096306)),
                captionText = "Marker in Pangyo"
            )
        }
    }
}
