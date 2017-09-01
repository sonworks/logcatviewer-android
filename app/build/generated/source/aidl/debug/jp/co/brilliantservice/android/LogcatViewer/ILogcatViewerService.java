/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/sonworks/Documents/LogcatViewer/app/src/main/aidl/jp/co/brilliantservice/android/LogcatViewer/ILogcatViewerService.aidl
 */
package jp.co.brilliantservice.android.LogcatViewer;
public interface ILogcatViewerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jp.co.brilliantservice.android.LogcatViewer.ILogcatViewerService
{
private static final java.lang.String DESCRIPTOR = "jp.co.brilliantservice.android.LogcatViewer.ILogcatViewerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jp.co.brilliantservice.android.LogcatViewer.ILogcatViewerService interface,
 * generating a proxy if needed.
 */
public static jp.co.brilliantservice.android.LogcatViewer.ILogcatViewerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jp.co.brilliantservice.android.LogcatViewer.ILogcatViewerService))) {
return ((jp.co.brilliantservice.android.LogcatViewer.ILogcatViewerService)iin);
}
return new jp.co.brilliantservice.android.LogcatViewer.ILogcatViewerService.Stub.Proxy(obj);
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
case TRANSACTION_getDispData:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.getDispData();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_saveLog:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.saveLog(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setLogBreak:
{
data.enforceInterface(DESCRIPTOR);
this.setLogBreak();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jp.co.brilliantservice.android.LogcatViewer.ILogcatViewerService
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
@Override public java.util.List<java.lang.String> getDispData() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDispData, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int saveLog(java.lang.String FileName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(FileName);
mRemote.transact(Stub.TRANSACTION_saveLog, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setLogBreak() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_setLogBreak, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getDispData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_saveLog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setLogBreak = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public java.util.List<java.lang.String> getDispData() throws android.os.RemoteException;
public int saveLog(java.lang.String FileName) throws android.os.RemoteException;
public void setLogBreak() throws android.os.RemoteException;
}
