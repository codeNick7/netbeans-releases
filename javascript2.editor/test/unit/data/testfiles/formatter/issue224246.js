var onLocalSdpSet = function()
{
    new
User(peerConnection.remoteUser).createConnection(peerConnection.localUser,
localSdp.sdp,
        function(userConnection)
        {
            console.log("Connection created: " + userConnection);
        }, onFailure);
    };