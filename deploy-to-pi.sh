#!/bin/bash
set -x
# Set your remote server's SSH details
SSH_USER="admin"
SSH_HOST="192.168.0.43"

#here you can delete folders that are not going to be used
#rm -r [YOUR_FOLDER_HERE]

#Put the whole project on a tar file
tar -zcvf payslip_reader.tar.gz payslip_reader

#Send project to PI
scp payslip_reader.tar.gz $SSH_USER@$SSH_HOST:/home/admin/docker-images

# SSH into the remote server
ssh $SSH_USER@$SSH_HOST << EOF
        set -x

        # Extract project contents
        tar -xvzf /home/admin/docker-images/payslip_reader.tar.gz -C /home/admin/docker-images/

    # Build the Project and The Docker image
        cd /home/admin/docker-images/payslip_reader
        mvn package
        docker-compose up

    # Optionally, push the image to a Docker registry
    # docker push your-registry-url/$IMAGE_NAME
EOF
