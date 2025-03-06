import SwiftUI
import shared_presentation

@main
struct iOSApp: App {

    init() {
        KoinAppKt.doInitKoin()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
