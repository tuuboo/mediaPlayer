/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\PlayerDemo\\Library\\YoukuPlayer\\src\\com\\youku\\libmanager\\ISoUpgradeService.aidl
 */
package com.youku.libmanager;
// Declare any non-default types here with import statements

public interface ISoUpgradeService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.youku.libmanager.ISoUpgradeService
{
private static final java.lang.String DESCRIPTOR = "com.youku.libmanager.ISoUpgradeService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.youku.libmanager.ISoUpgradeService interface,
 * generating a proxy if needed.
 */
public static com.youku.libmanager.ISoUpgradeService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.youku.libmanager.ISoUpgradeService))) {
return ((com.youku.libmanager.ISoUpgradeService)iin);
}
return new com.youku.libmanager.ISoUpgradeService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_startDownloadSo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.startDownloadSo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isSoDownloaded:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.isSoDownloaded(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
com.youku.libmanager.ISoUpgradeCallback _arg0;
_arg0 = com.youku.libmanager.ISoUpgradeCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.youku.libmanager.ISoUpgradeService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void startDownloadSo(java.lang.String soName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(soName);
mRemote.transact(Stub.TRANSACTION_startDownloadSo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isSoDownloaded(java.lang.String soName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(soName);
mRemote.transact(Stub.TRANSACTION_isSoDownloaded, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void registerCallback(com.youku.libmanager.ISoUpgradeCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startDownloadSo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_isSoDownloaded = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void startDownloadSo(java.lang.String soName) throws android.os.RemoteException;
public boolean isSoDownloaded(java.lang.String soName) throws android.os.RemoteException;
public void registerCallback(com.youku.libmanager.ISoUpgradeCallback callback) throws android.os.RemoteException;
}
