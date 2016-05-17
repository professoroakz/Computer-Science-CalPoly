def get_byte_value(frame):
    return int(frame) & 0xFF

def main():
    print get_byte_value(23423)

if __name__ == '__main__':
    main()