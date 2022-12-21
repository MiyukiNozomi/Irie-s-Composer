#include <stdio.h>
#include <windows.h>
#include <ShObjIdl.h>

extern "C" {
    LPWSTR Platform_OpenFileDialog(bool folders) {
        HRESULT hr = CoInitializeEx(NULL, COINIT_APARTMENTTHREADED | 
        COINIT_DISABLE_OLE1DDE);

        if (SUCCEEDED(hr)) {
            IFileOpenDialog *pFileOpen;
            // Create the FileOpenDialog object
            hr = CoCreateInstance(CLSID_FileOpenDialog, NULL, CLSCTX_ALL, 
                IID_IFileOpenDialog, reinterpret_cast<void**>(&pFileOpen));
            if (SUCCEEDED(hr)) {
                if (folders) {
                    DWORD dwOptions;
                    if (SUCCEEDED(pFileOpen->GetOptions(&dwOptions))) {
                        pFileOpen->SetOptions(dwOptions | FOS_PICKFOLDERS);
                    }
                }
                // Show the open dialog box
                hr = pFileOpen->Show(NULL);

                // Get the file name from the dialog box
                if (SUCCEEDED(hr)) {
                    IShellItem *pItem;
                    hr = pFileOpen->GetResult(&pItem);
                    if (SUCCEEDED(hr)) {
                        LPWSTR pszFilePath;// LPWSTR pszFileName;
                        hr = pItem->GetDisplayName(SIGDN_FILESYSPATH, &pszFilePath);
                        if (SUCCEEDED(hr)) {
                            wprintf(L"irie.platform:folderselect.cpp > LPWSTR: %s\n", pszFilePath);

                            pItem->Release();
                            CoUninitialize();

                            return pszFilePath;
                        }
                        pItem->Release();
                    }
                }
            }
            CoUninitialize();
        }

        return NULL;
    }

    void FreeLPWSTR(LPWSTR str) {
        CoTaskMemFree(str);
    }
}